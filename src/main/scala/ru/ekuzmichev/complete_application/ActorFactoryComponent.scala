package ru.ekuzmichev.complete_application

trait ActorFactory {
  def createMasterActor(): Master
  def createWorkerActor(): Worker
}

trait ActorFactoryComponent {
  this: AppConfigComponent with DaoServiceComponent =>

  val actorFactory: ActorFactory

  class ActorFactoryImpl extends ActorFactory {
    override def createMasterActor(): Master = new Master(appConfigService.workers, this)
    override def createWorkerActor(): Worker = new Worker(daoService)
  }
}
