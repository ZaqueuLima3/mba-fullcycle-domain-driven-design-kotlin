package dev.zaqueu.domaindrivendesignkotlin.core.common.domain.domainevents

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component

@Component
internal class EventEmitter {
    private val _events = MutableSharedFlow<Pair<String, DomainEvent>>()
    val events = _events.asSharedFlow()

    fun on(event: String, handler: (DomainEvent) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            events
                .filter { it.first.startsWith(event) }
                .collect { (_, event) -> handler(event) }
        }
    }

    fun emit(name: String, event: DomainEvent) {
        CoroutineScope(Dispatchers.IO).launch {
            _events.emit(name to event)
        }
    }
}
