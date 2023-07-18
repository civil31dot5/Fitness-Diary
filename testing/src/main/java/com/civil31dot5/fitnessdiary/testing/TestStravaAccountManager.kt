package com.civil31dot5.fitnessdiary.testing

import com.civil31dot5.fitnessdiary.domain.model.StravaConnectStatus
import com.civil31dot5.fitnessdiary.domain.usecase.sport.StravaAccountManager

class TestStravaAccountManager: StravaAccountManager {

    var isConnectStravaCalled = false

    private var connectStravaResult = false
    fun setConnectStravaResult(result: Boolean){
        connectStravaResult = result
    }
    override suspend fun connectStrava(): Boolean {
        isConnectStravaCalled = true
        return connectStravaResult
    }

    private var stravaConnectStatus = StravaConnectStatus.NotConnected
    fun setStravaConnectStatus(status: StravaConnectStatus){
        stravaConnectStatus = status
    }
    override fun connectStatus(): StravaConnectStatus {
        return stravaConnectStatus
    }

    var isDisconnectStravaCalled = false
    override fun disconnectStrava() {
        isDisconnectStravaCalled = true
    }
}