package ru.ekuzmichev.complete_application

import org.mockito.MockitoSugar

trait TestEnvironment
    extends AppConfigComponent
    with IoServiceComponent
    with JobConfigReaderServiceComponent
    with DatabaseServiceComponent
    with MigrationComponent
    with DaoServiceComponent
    with ActorFactoryComponent
    with MockitoSugar {

  // use the test configuration file.
  override val appConfigService: AppConfigService = spy(new AppConfigService)
  // override the path here to use the test resources.
  when(appConfigService.configPath).thenReturn(this.getClass.getResource("/").getPath)

  override val ioService: IoService                           = mock[IoService]
  override val jobConfigReaderService: JobConfigReaderService = mock[JobConfigReaderService]
  override val databaseService: DatabaseService               = mock[DatabaseService]
  override val migrationService: MigrationService             = mock[MigrationService]
  override val daoService: DaoService                         = mock[DaoService]
  override val actorFactory: ActorFactory                     = mock[ActorFactory]
}
