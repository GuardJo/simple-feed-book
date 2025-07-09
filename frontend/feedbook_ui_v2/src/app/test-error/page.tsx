"use client"

import {Card} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import {useState} from "react";
import {notFound, useRouter} from "next/navigation";

/**
 * 에러 테스트용 페이지
 */
export default function TestErrorPage() {
    if (process.env.NODE_ENV !== "development") {
        notFound()
    }

    const router = useRouter()
    const [shouldError, setShouldError] = useState(false)

    const handleErrorEvent = () => {
        setShouldError(true);
    }

    const throwError = () => {
        throw new Error("에러 테스트")
    }

    const goToUnknownPage = () => {
        router.push("/unknown-page")
    }

    return (
        <div className="flex-1 max-w-2xl mx-auto w-full p-4 pb-20">
            <h1 className="text-2xl font-bold mb-6">에러 테스트 페이지</h1>

            <div className="space-y-4">
                <Card className="p-6">
                    <h2 className="text-lg font-semibold mb-4">일반 에러 테스트</h2>
                    <p className="text-gray-600 mb-4">버튼을 클릭하면 에러가 발생하여 error.tsx 페이지가 표시됩니다.</p>
                    <Button variant="destructive" onClick={() => handleErrorEvent()}>
                        에러 발생
                    </Button>
                    {shouldError && (throwError())}
                </Card>

                <Card className="p-6">
                    <h2 className="text-lg font-semibold mb-4">잘못된 페이지 접근 테스트</h2>
                    <p className="text-gray-600 mb-4">존재하지 않는 페이지로 이동하여 not-found.tsx 페이지가 표시됩니다.</p>
                    <Button variant="outline" onClick={() => goToUnknownPage()}>
                        페이지 이동
                    </Button>
                </Card>
            </div>
        </div>
    )
}