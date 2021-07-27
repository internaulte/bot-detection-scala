package modules.trafficloadbalancingmock.domain.usecases

import modules.trafficloadbalancingmock.adapters.repositories.interfaces.HttpTrafficSend

class HttpTrafficGenerationUseCases(private val httpTrafficSendRepository: HttpTrafficSend) {}
