package com.civil31dot5.fitnessdiary.domain.usecase.sport

import javax.inject.Inject

class DisconnectStravaUseCase @Inject constructor(
    private val stravaAccountManager: StravaAccountManager
) {

    operator fun invoke(){
        stravaAccountManager.disconnectStrava()
    }
}