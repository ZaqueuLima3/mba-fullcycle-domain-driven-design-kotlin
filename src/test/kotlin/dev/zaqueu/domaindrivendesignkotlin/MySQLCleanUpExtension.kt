package dev.zaqueu.domaindrivendesignkotlin

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.Repository
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.test.context.junit.jupiter.SpringExtension

class MySQLCleanUpExtension : BeforeEachCallback {
    override fun beforeEach(context: ExtensionContext) {
        val repositories = getRepositories(context)
        cleanUp(repositories)
    }

    private fun getRepositories(context: ExtensionContext): MutableCollection<Repository<*>> {
        val applicationContext = SpringExtension.getApplicationContext(context)
        return applicationContext.getBeansOfType(Repository::class.java).values
    }

    private fun cleanUp(repositories: MutableCollection<Repository<*>>) {
        repositories.forEach { repository ->
            repository.deleteAll()
        }
    }
}
