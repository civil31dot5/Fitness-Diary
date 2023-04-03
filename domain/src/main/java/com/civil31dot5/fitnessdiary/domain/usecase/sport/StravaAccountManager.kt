package com.civil31dot5.fitnessdiary.domain.usecase.sport

import com.civil31dot5.fitnessdiary.domain.model.StravaConnectStatus

interface StravaAccountManager {
    suspend fun connectStrava(): Boolean
    fun connectStatus(): StravaConnectStatus
    fun disconnectStrava()
}