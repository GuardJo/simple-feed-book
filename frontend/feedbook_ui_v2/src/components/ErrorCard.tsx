"use client"

import {Card} from "@/components/ui/card";
import {AlertTriangle, Home, RefreshCw} from "lucide-react";
import {Button} from "@/components/ui/button";
import {useRouter} from "next/navigation";
import {useEffect} from "react";

/**
 * 전역 에러 메시지 카드 컴포넌트
 */
export default function ErrorCard({error, reset, isDevMode = false}: ErrorCardProps) {
    const router = useRouter();

    const goHome = () => {
        router.replace("/")
    }

    useEffect(() => {
        console.error(`Error: ${error}`);
    }, [error]);

    return (
        <div className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
            <Card className="max-w-md w-full p-8 text-center">
                <div className="flex justify-center mb-6">
                    <div className="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center">
                        <AlertTriangle className="w-8 h-8 text-red-500"/>
                    </div>
                </div>

                <h1 className="text-2xl font-bold text-gray-900 mb-4">문제가 발생했습니다</h1>

                <p className="text-gray-600 mb-6">예상치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요.</p>

                {isDevMode && (
                    <div className="bg-red-50 border border-red-200 rounded-md p-4 mb-6 text-left">
                        <h3 className="text-sm font-medium text-red-800 mb-2">개발 모드 에러 정보 : </h3>
                        <p className="text-xs text-red-700 font-mono break-all">{error.message}</p>
                    </div>
                )}

                <div className="flex flex-col sm:flex-row gap-3">
                    <Button onClick={() => reset()} className="flex-1 bg-blue-500 hover:bg-blue-600 text-white">
                        <RefreshCw className="w-4 h-4 mr-2"/>
                        다시 시도
                    </Button>
                    <Button variant="outline" onClick={goHome} className="flex-1">
                        <Home className="w-4 h-4 mr-2"/>
                        홈으로 이동
                    </Button>
                </div>
            </Card>
        </div>
    )
}

interface ErrorCardProps {
    error: Error,
    reset: () => void,
    isDevMode: boolean,
}