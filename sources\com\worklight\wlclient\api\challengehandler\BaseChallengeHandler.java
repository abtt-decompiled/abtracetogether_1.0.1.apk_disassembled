package com.worklight.wlclient.api.challengehandler;

import com.worklight.common.Logger;
import com.worklight.wlclient.WLRequest;
import com.worklight.wlclient.api.WLErrorCode;
import com.worklight.wlclient.api.WLFailResponse;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseChallengeHandler<T> {
    protected WLRequest activeRequest = null;
    private String handlerName;
    private List<WLRequest> requestWaitingList = new ArrayList();

    public abstract void handleChallenge(T t);

    public String getHandlerName() {
        String str = "getHandlerName";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.handlerName;
    }

    protected BaseChallengeHandler(String str) {
        this.handlerName = str;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0033, code lost:
        handleChallenge(r4);
        com.worklight.common.Logger.exit(getClass().getSimpleName(), "startHandleChallenge");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0043, code lost:
        return;
     */
    public void startHandleChallenge(WLRequest wLRequest, T t) {
        Logger.enter(getClass().getSimpleName(), "startHandleChallenge");
        synchronized (this) {
            if (!wLRequest.getOptions().isFromChallenge()) {
                if (this.activeRequest != null) {
                    this.requestWaitingList.add(wLRequest);
                    Logger.exit(getClass().getSimpleName(), "startHandleChallenge");
                    return;
                }
                this.activeRequest = wLRequest;
            }
        }
    }

    public void cancel() {
        String str = "cancel";
        Logger.enter(getClass().getSimpleName(), str);
        if (this.activeRequest.shouldFailOnChallengeCancel()) {
            this.activeRequest.processFailureResponse(new WLFailResponse(WLErrorCode.CHALLENGE_HANDLING_CANCELED, WLErrorCode.CHALLENGE_HANDLING_CANCELED.getDescription(), this.activeRequest.getOptions()));
        }
        clearChallengeRequests();
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void clearChallengeRequests() {
        Logger.enter(getClass().getSimpleName(), "clearChallengeRequests");
        synchronized (this) {
            this.activeRequest = null;
            clearWaitingList();
        }
        Logger.exit(getClass().getSimpleName(), "clearChallengeRequests");
    }

    public synchronized void releaseWaitingList() {
        Logger.enter(getClass().getSimpleName(), "releaseWaitingList");
        for (WLRequest removeExpectedAnswer : this.requestWaitingList) {
            removeExpectedAnswer.removeExpectedAnswer(this.handlerName);
        }
        clearWaitingList();
        Logger.exit(getClass().getSimpleName(), "releaseWaitingList");
    }

    public synchronized void clearWaitingList() {
        Logger.enter(getClass().getSimpleName(), "clearWaitingList");
        this.requestWaitingList.clear();
        Logger.exit(getClass().getSimpleName(), "clearWaitingList");
    }
}
