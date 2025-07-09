"use client"

import ErrorCard from "@/components/ErrorCard";

/**
 * 전역 에러 페이지
 */
export default function Error({error, reset}: { error: Error & { digest?: string }, reset: () => void }) {
    return (
        <div className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
            <ErrorCard error={error} reset={reset} isDevMode={process.env.NODE_ENV === "development"}/>
        </div>
    )
}