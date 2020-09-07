package com.diacono.rediget.reader.domain.model.exception

class TopPostException () : Exception() {
    override var message: String = "There was an error getting the top posts"
}