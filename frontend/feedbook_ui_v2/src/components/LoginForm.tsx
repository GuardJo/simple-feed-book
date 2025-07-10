"use client"

import FocusingInput from "@/components/FocusingInput";
import Link from "next/link";
import {FormEvent, useEffect} from "react";
import {login, LoginRequest} from "@/lib/accountApiCaller";
import {useMutation} from "@tanstack/react-query";
import {getAccessToken, setAccessToken} from "@/lib/utils";
import {useRouter} from "next/navigation";

/**
 * 로그인 폼 컴포넌트
 */
export default function LoginForm() {
    const router = useRouter()
    const loginMutation = useMutation({
            mutationKey: ["login"],
            mutationFn: (req: LoginRequest) => login(req),
            onSuccess: data => {
                if (data.status === "OK") {
                    setAccessToken(data.body)
                    router.push("/")
                } else if (data.status === "NOT_FOUND") {
                    window.alert('해당 계정 정보를 찾을 수 없습니다.')
                } else {
                    window.alert(data.body)
                }
            },
            onError: error => {
                window.alert(error.message)
            }
        }
    )

    useEffect(() => {
        // 이미 로그인한 사용자는 로그인페이지 접근 시 이전 페이지로 강제 이동
        try {
            const token = getAccessToken()

            if (token !== '' && token !== null && token !== undefined) {
                router.back()
            }
        } catch {
            return
        }
    }, [router]);

    const handleLogin = (event: FormEvent<HTMLFormElement>): void => {
        event.preventDefault()
        const formData: FormData = new FormData(event.target as HTMLFormElement)
        const loginRequest: LoginRequest = {
            username: formData.get("username")!.toString(),
            password: formData.get("password")!.toString()
        }

        loginMutation.mutate(loginRequest)
    }

    return (
        <div className="flex-1 flex flex-col justify-center items-center p-4">
            <div className="w-full max-w-md px-4">
                <h1 className="text-4xl font-bold text-center mb-12">Login</h1>
                <form onSubmit={handleLogin} className="space-y-8">
                    <div className="relative">
                        <FocusingInput id="username" labelName="ID" type="text"/>
                    </div>
                    <div className="relative">
                        <FocusingInput id="password" labelName="password" type="password"/>
                    </div>
                    <div className="pt-8 flex flex-col items-center">
                        <button type="submit"
                                className="bg-gray-100 hover:bg-gray-300 text-black rounded-full py-3 px-12 text-lg font-medium">
                            Login
                        </button>
                        <Link href="/signup" className="mt-6 text-blue-500 hover:underline">
                            Sign up
                        </Link>
                    </div>
                </form>
            </div>
        </div>
    )
}