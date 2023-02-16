package com.phodal.chapi.arrow.core

public class DomainConstants {
    companion object {
        val ASSERTION_ArrayList = arrayListOf(
            "assert",
            "should",
            "check",    // ArchUnit,
            "maynotbe", // ArchUnit,
            "is",       // RestAssured,
            "spec",     // RestAssured,
            "verify"   // Mockito,
        )
    }
}
