package ru.ekuzmichev.complete_application

object ComponentRegistry
    extends AppConfigComponent
    with IoServiceComponent
    with JobConfigReaderServiceComponent
    with DatabaseServiceComponent
    with MigrationComponent
    with DaoServiceComponent
    with ActorFactoryComponent {

  override val appConfigService: ComponentRegistry.AppConfigService             = new AppConfigService
  override val ioService: ComponentRegistry.IoService                           = new IoService
  override val jobConfigReaderService: ComponentRegistry.JobConfigReaderService = new JobConfigReaderService
  override val databaseService: DatabaseService                                 = new H2DatabaseService
  override val migrationService: ComponentRegistry.MigrationService             = new MigrationService
  override val daoService: DaoService                                           = new DaoServiceImpl
  override val actorFactory: ActorFactory                                       = new ActorFactoryImpl
}
