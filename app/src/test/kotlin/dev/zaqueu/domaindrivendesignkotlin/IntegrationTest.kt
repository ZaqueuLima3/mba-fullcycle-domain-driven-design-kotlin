package dev.zaqueu.domaindrivendesignkotlin

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.lang.annotation.Inherited


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MySQLCleanUpExtension::class)
annotation class IntegrationTest
