package com.civil31dot5.fitnessdiary.domain.usecase.sport

import com.civil31dot5.fitnessdiary.domain.model.StravaConnectStatus
import javax.inject.Inject

class GetStravaConnectStatusUseCase @Inject constructor(
    private val stravaAccountManager: StravaAccountManager
) {

    operator fun invoke(): StravaConnectStatus{
        return stravaAccountManager.connectStatus()
    }
}