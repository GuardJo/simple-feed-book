"use client"

import ErrorCard from "@/components/ErrorCard";

/**
 * 전역 에러 페이지
 */
export default function Error({error, reset}: { error: Error & { digest?: string }, reset: () => void }) {
    return <ErrorCard error={error} reset={reset} isDevMode={process.env.NODE_ENV === "development"}/>
}