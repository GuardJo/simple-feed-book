"use client"

import FocusingInput from "@/components/FocusingInput";
import Link from "next/link";
import {FormEvent} from "react";

/**
 * 로그인 폼 컴포넌트
 */
export default function LoginForm() {
    const handleLogin = (event: FormEvent<HTMLFormElement>): void => {
        event.preventDefault()
        const formData: FormData = new FormData(event.target as HTMLFormElement)

        // TODO API 연동
        console.log(`ID : ${formData.get("username")}, password : ${formData.get("password")}`)
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