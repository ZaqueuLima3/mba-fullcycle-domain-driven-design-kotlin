package dev.zaqueu.domaindrivendesignkotlin

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.lang.annotation.Inherited


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@ActiveProfiles("test-e2e")
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MySQLCleanUpExtension::class)
@AutoConfigureMockMvc
annotation class E2ETest
