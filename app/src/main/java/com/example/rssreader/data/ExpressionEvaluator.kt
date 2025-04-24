package com.example.rssreader.data

object ExpressionEvaluator {
    fun evaluate(expression: String, tagsSet: Set<String>): Boolean {
        if (expression.isBlank()) return true

        // Handle simple tag matching (most common case)
        if (!expression.contains("&&") && !expression.contains("||") && !expression.contains("!")) {
            val tag = expression.trim().lowercase()
            return tagsSet.contains(tag)
        }

        // Parse complex expressions
        return try {
            val tokens = expression.replace("&&", " && ")
                .replace("||", " || ")
                .replace("!", " ! ")
                .split(" ")
                .filter { it.isNotBlank() }

            evaluateTokens(tokens, 0, tagsSet).first
        } catch (e: Exception) {
            false
        }
    }

    private fun evaluateTokens(
        tokens: List<String>,
        startIndex: Int,
        tagsSet: Set<String>
    ): Pair<Boolean, Int> {
        if (startIndex >= tokens.size) {
            return false to startIndex
        }

        var result = false
        var i = startIndex
        var operator = "OR" // Default join

        while (i < tokens.size) {
            when (val token = tokens[i]) {
                "&&" -> { operator = "AND"; i++ }
                "||" -> { operator = "OR"; i++ }
                "!" -> {
                    val (nextResult, nextIndex) = evaluateTokens(tokens, i + 1, tagsSet)
                    val value = !nextResult
                    result = when (operator) {
                        "AND" -> result && value
                        else -> result || value
                    }
                    i = nextIndex
                }
                else -> {
                    val value = tagsSet.contains(token.lowercase())
                    result = when (operator) {
                        "AND" -> result && value
                        else -> result || value
                    }
                    i++
                }
            }
        }

        return result to i
    }
}