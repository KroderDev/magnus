package dev.kroder.magnus.domain.messaging

/**
 * Interface for a messaging system (Pub/Sub).
 * Allows sending messages to channels and subscribing to them.
 */
interface MessageBus {
    /**
     * Publishes a message to a specific channel.
     * @param channel The channel name.
     * @param message The message content (usually JSON).
     */
    fun publish(channel: String, message: String)

    /**
     * Subscribes to a channel to receive messages.
     * @param channel The channel name.
     * @param callback Function to execute when a message is received.
     */
    fun subscribe(channel: String, callback: (message: String) -> Unit)

    /**
     * Checks if the message bus is currently actively subscribed to a channel.
     * @param channel The channel name.
     * @return True if actively subscribed.
     */
    fun isSubscribed(channel: String): Boolean = false

    /**
     * Closes the connection to the message bus.
     */
    fun close()
}
