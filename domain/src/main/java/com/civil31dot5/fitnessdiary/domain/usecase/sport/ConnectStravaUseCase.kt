package com.civil31dot5.fitnessdiary.domain.usecase.sport

import javax.inject.Inject

class ConnectStravaUseCase @Inject constructor(
    private val stravaAccountManager: StravaAccountManager
) {

    suspend operator fun invoke(): Boolean {
        return stravaAccountManager.connectStrava()
    }

}