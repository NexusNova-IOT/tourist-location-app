package pe.upc.tourist_location.location.model

data class AuthResponse(
    val kind: String,
    val localId: String,
    val email: String,
    val displayName: String?,
    val idToken: String,
    val registered: Boolean,
    val refreshToken: String,
    val expiresIn: String
)

