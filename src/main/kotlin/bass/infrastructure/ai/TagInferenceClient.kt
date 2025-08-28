package bass.infrastructure.ai

import bass.dto.tag.TagInferenceResultDTO
import bass.exception.AiInferenceException
import bass.exception.InvalidTagNameException
import bass.exception.OperationFailedException
import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Component

@Component
class TagInferenceClient(
    private val chatClient: ChatClient,
) {
    fun inferTags(
        userQuery: String,
        allowedTags: Set<String>,
        maxTags: Int = 8,
    ): TagInferenceResultDTO {
        if (userQuery.isBlank()) {
            throw InvalidTagNameException("User query must not be blank")
        }
        if (allowedTags.isEmpty()) {
            throw OperationFailedException("Allowed tags list must not be empty")
        }

        val systemPrompt = createSystemPrompt(allowedTags, maxTags)
        val userPrompt = createUserPrompt(userQuery)

        try {
            val result =
                chatClient
                    .prompt()
                    .system(systemPrompt)
                    .user(userPrompt)
                    .call()
                    .entity(TagInferenceResultDTO::class.java)

            return result?.takeIf { it.selectedTags.isNotEmpty() }
                ?: TagInferenceResultDTO()
        } catch (ex: Exception) {
            throw AiInferenceException("Failed to infer tags for query: '$userQuery'", ex)
        }
    }

    private fun createSystemPrompt(
        allowedTags: Set<String>,
        maxTags: Int,
    ): String {
        return """
            You are an expert meal recommendation assistant.
            Your task: Analyze the user's input and return only relevant meal tags.

            RULES:
            - Output must be a valid JSON object with exactly one key: "selectedTags".
              Example: { "selectedTags": ["vegan", "spicy"] }
            - Always return at least 1 tag. If no exact match exists, return the closest related tag.
            - Use only tags from the allowed list below. Do NOT create new tags.
            - Select between 1 and $maxTags tags.
            - All tags must be lowercase.
            - Do NOT include explanations, comments, or any text outside the JSON object.

            Allowed tags: ${allowedTags.sorted().joinToString(", ")}
            """.trimIndent()
    }

    private fun createUserPrompt(userQuery: String): String {
        return """
            User request: "$userQuery"

            Select the most relevant tags from the allowed tags.
            If no exact match exists, choose the closest related tag.
            Respond ONLY with the JSON object defined in the system prompt.
            """.trimIndent()
    }
}
