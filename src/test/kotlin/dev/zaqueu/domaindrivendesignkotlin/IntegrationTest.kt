package dev.zaqueu.domaindrivendesignkotlin

import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.data.repository.CrudRepository
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.junit.jupiter.Testcontainers
import java.lang.annotation.Inherited
import java.util.function.Consumer

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MySQLCleanUpExtension::class)
annotation class IntegrationTest

class MySQLCleanUpExtension : BeforeEachCallback {
    override fun beforeEach(context: ExtensionContext) {
        val repositories: Collection<CrudRepository<*, *>> = SpringExtension.getApplicationContext(context)
            .getBeansOfType(CrudRepository::class.java)
            .values
        cleanUp(repositories)
    }

    private fun cleanUp(repositories: Collection<CrudRepository<*, *>>) {
        repositories.forEach(Consumer { obj: CrudRepository<*, *> -> obj.deleteAll() })
    }
}
