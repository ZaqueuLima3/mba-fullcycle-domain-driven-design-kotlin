package dev.zaqueu.domaindrivendesignkotlin

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.ValueObject
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
