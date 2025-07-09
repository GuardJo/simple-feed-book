"use client"

import {useRouter} from "next/navigation";
import {Card} from "@/components/ui/card";
import {ArrowLeft, FileQuestion, Home} from "lucide-react";
import {Button} from "@/components/ui/button";

/**
 * 404 에러 처리용 카드 컴포넌트
 */
export default function NotFoundCard() {
    const router = useRouter()

    const handleGotoHome = () => {
        router.push("/")
    }

    const handleGotoBack = () => {
        router.back()
    }

    return (
        <Card className="max-w-md w-full p-8 text-center">
            <div className="flex justify-center mb-6">
                <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center">
                    <FileQuestion className="w-8 h-8 text-blue-500"/>
                </div>
            </div>

            <h1 className="text-2xl font-bold text-gray-900 mb-4">페이지를 찾을 수 없습니다</h1>

            <p className="text-gray-600 mb-6">
                요청하신 페이지가 존재하지 않거나 이동되었을 수 있습니다.
                <br/>
                URL을 다시 확인해주세요.
            </p>

            <div className="flex flex-col sm:flex-row gap-3">
                <Button onClick={handleGotoHome}
                        className="flex-1 bg-blue-500 hover:bg-blue-600 text-white">
                    <Home className="w-4 h-4 mr-2"/>
                    홈으로 이동
                </Button>
                <Button variant="outline" onClick={handleGotoBack} className="flex-1">
                    <ArrowLeft className="w-4 h-4 mr-2"/>
                    이전 페이지
                </Button>
            </div>
        </Card>
    )
}