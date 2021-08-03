package modules.trafficanalysis.jobs.interfaces

import scala.concurrent.Future

trait TrafficAnalysisJobs {
  def startTrafficAnalysis: Future[Unit]
}
