package com.therishka.androidlab_2.network;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.therishka.androidlab_2.models.VkDialogResponse;
import com.therishka.androidlab_2.models.VkFriend;
import com.therishka.androidlab_2.models.VkGroup;
import com.therishka.androidlab_2.models.VkNewsItem;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rx.AsyncEmitter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;

/**
 * @author Rishad Mustafaev
 */

public class RxVk {

    public void getUsers(final RxVkListener<VKList<VKApiUser>> listener, long userId) {
        Subscriber<VKList<VKApiUser>> usersSubscriber = Subscribers.create(new Action1<VKList<VKApiUser>>() {
            @Override
            public void call(VKList<VKApiUser> vkApiUsers) {
                listener.requestFinished(vkApiUsers);
            }
        });
        pGetUsers(userId).subscribe(usersSubscriber);
    }

    public void getFriends(final RxVkListener<List<VkFriend>> listener) {
        Subscriber<List<VkFriend>> friendsSubscriber = Subscribers.create(new Action1<List<VkFriend>>() {
            @Override
            public void call(List<VkFriend> vkApiUsers) {
                listener.requestFinished(vkApiUsers);
            }
        });
        pGetFriends(VKParameters.from("order", "hints", "fields", "name, photo_50, online"))
                .subscribe(friendsSubscriber);

    }

    public void getDialogs(final RxVkListener<VkDialogResponse> listener) {
        Subscriber<VkDialogResponse> dialogsSubscriber = Subscribers.create(new Action1<VkDialogResponse>() {
            @Override
            public void call(VkDialogResponse vkDialogResponse) {
                listener.requestFinished(vkDialogResponse);
            }
        });
        pGetDialogs().subscribe(dialogsSubscriber);
    }

    public void getNews(final RxVkListener<LinkedList<VkNewsItem>> listener) {
        Subscriber<LinkedList<VkNewsItem>> newsSubscriber = Subscribers.create(new Action1<LinkedList<VkNewsItem>>() {
            @Override
            public void call(LinkedList<VkNewsItem> vkNewsItems) {
                listener.requestFinished(vkNewsItems);
            }
        });
        pGetNews().subscribe(newsSubscriber);
    }

    private Observable<VKList<VKApiUser>> pGetUsers(final long userId) {
        return Observable.fromEmitter(new Action1<AsyncEmitter<VKResponse>>() {
            @Override
            public void call(final AsyncEmitter<VKResponse> vkResponseAsyncEmitter) {
                VKParameters parameters = userId != 0 ? VKParameters.from(VKApiConst.USER_IDS, userId)
                        : null;
                VKApi.users().get(parameters).executeWithListener(new RxVkRequestListener(vkResponseAsyncEmitter));
            }
        }, AsyncEmitter.BackpressureMode.LATEST)
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // do nothing
                    }
                })
                .flatMap(new Func1<VKResponse, Observable<VKList<VKApiUser>>>() {
                    @Override
                    public Observable<VKList<VKApiUser>> call(VKResponse vkResponse) {
                        Object parsedModel = vkResponse.parsedModel;
                        if (parsedModel != null && parsedModel instanceof VKList) {
                            VKList list = ((VKList) parsedModel);
                            if (checkCast(VKApiUser.class, list)) {
                                //noinspection unchecked
                                return Observable.just(((VKList<VKApiUser>) parsedModel));
                            }
                        }
                        return Observable.empty();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<VkFriend>> pGetFriends(final VKParameters params) {
        return Observable.fromEmitter(new Action1<AsyncEmitter<VKResponse>>() {
            @Override
            public void call(AsyncEmitter<VKResponse> vkResponseAsyncEmitter) {
                VKApi.friends().get(params)
                        .executeWithListener(new RxVkRequestListener(vkResponseAsyncEmitter));
            }
        }, AsyncEmitter.BackpressureMode.LATEST)
                .flatMap(new Func1<VKResponse, Observable<List<VkFriend>>>() {
                    @Override
                    public Observable<List<VkFriend>> call(VKResponse vkResponse) {
                        Object parsedModel = vkResponse.parsedModel;
                        if (parsedModel != null && parsedModel instanceof VKList) {
                            VKList list = ((VKList) parsedModel);
                            if (checkCast(VKApiUser.class, list)) {
                                //noinspection unchecked
                                VKList<VKApiUser> castedList = ((VKList<VKApiUser>) list);
                                List<VkFriend> friends = new ArrayList<>();
                                for (VKApiUser user : castedList) {
                                    VkFriend friend = new VkFriend();
                                    friend.setName(user.first_name);
                                    friend.setSurname(user.last_name);
                                    friend.setOnline(user.online);
                                    friend.setSmallPhotoUrl(user.photo_50);
                                    friends.add(friend);
                                }
                                return Observable.just(friends);
                            }
                        }
                        return Observable.empty();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<LinkedList<VkNewsItem>> pGetNews() {
        return Observable.fromEmitter(new Action1<AsyncEmitter<VKResponse>>() {
            @Override
            public void call(AsyncEmitter<VKResponse> linkedListAsyncEmitter) {
                new VKRequest("newsfeed.get", VKParameters.from("count", 20, "source_ids", "groups"))
                        .executeWithListener(new RxVkRequestListener(linkedListAsyncEmitter));
            }
        }, AsyncEmitter.BackpressureMode.LATEST)
                .flatMap(new Func1<VKResponse, Observable<LinkedList<VkNewsItem>>>() {
                    @Override
                    public Observable<LinkedList<VkNewsItem>> call(VKResponse response) {
                        try {
                            JSONObject responseObj = response.json.getJSONObject("response");
                            JSONArray itemsObj = responseObj.getJSONArray("items");
                            JSONArray groupsObj = responseObj.getJSONArray("groups");

                            Gson gson = new Gson();
                            List<VkNewsItem> newsItems = gson.fromJson(itemsObj.toString(), new TypeToken<List<VkNewsItem>>() {
                            }.getType());

                            List<VkGroup> groups = gson.fromJson(groupsObj.toString(),
                                    new TypeToken<List<VkGroup>>() {
                                    }.getType());

                            LinkedList<VkNewsItem> result = new LinkedList<>();

                            for (VkNewsItem item : newsItems) {
                                for (VkGroup group : groups) {
                                    if (Math.abs(item.getSource_id()) == Math.abs(group.getId())) {
                                        item.setPublisher(group);
                                    }
                                }
                                result.add(item);
                            }
                            return Observable.just(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return Observable.empty();
                    }
                });
    }

    private Observable<VkDialogResponse> pGetDialogs() {
        return Observable.fromEmitter(new Action1<AsyncEmitter<VKResponse>>() {
            @Override
            public void call(AsyncEmitter<VKResponse> responseEmitter) {
                new VKRequest("execute", VKParameters.from("code", getJavaScriptCode()))
                        .executeWithListener(new RxVkRequestListener(responseEmitter));
            }
        }, AsyncEmitter.BackpressureMode.LATEST)
                .flatMap(new Func1<VKResponse, Observable<VkDialogResponse>>() {
                    @Override
                    public Observable<VkDialogResponse> call(VKResponse vkResponse) {
                        try {
                            String responseObjectIgnored = vkResponse.json.getJSONObject("response").toString();
                            Gson gson = new Gson();
                            VkDialogResponse response = gson.fromJson(responseObjectIgnored, VkDialogResponse.class);
                            return Observable.just(response);
                        } catch (JSONException e) {
                            return Observable.empty();
                        }
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private boolean checkCast(Class classz, @NonNull VKList list) {
        return !list.isEmpty() && list.get(0) != null && (classz.isInstance(list.get(0)));
    }

    public interface RxVkListener<T> {
        void requestFinished(T requestResult);
    }

    // sorry for that shitcode. But that damn VKAPI doesn't provide other options.
    private String getJavaScriptCode() {
        return "var dialogs = API.messages.getDialogs({\"count\":20}); " +
                "var items = dialogs.items; " +
                "var size = dialogs.items.length; " +
                "var counter = 0; " +
                "var parItem = []; " +
                "while(counter < size){ " +
                "parItem.push( API.users.get({\"user_ids\":items[counter].message.user_id, " +
                "\"fields\":\"photo_50\"})[0]); counter = counter+1; " +
                "} counter = 0; " +
                "size = parItem.length; " +
                "var result = []; " +
                "while(counter < size){ " +
                "result.push({\"username\" : parItem[counter].first_name " +
                "+ \" \" + parItem[counter].last_name, " +
                "\"title\": items[counter].message.title, " +
                "\"body\":items[counter].message.body, " +
                "\"photo_50\":parItem[counter].photo_50, " +
                "\"is_read\":items[counter].message.read_state});" +
                "counter = counter +1; } " +
                "\n" +
                "return {\"count\":size, \"dialogs\":result};";
    }
}
