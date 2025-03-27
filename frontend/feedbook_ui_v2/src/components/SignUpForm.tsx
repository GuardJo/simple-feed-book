"use client"

import FocusingInput from "@/components/FocusingInput";
import Link from "next/link";
import {FormEvent} from "react";
import {useMutation} from "@tanstack/react-query";
import {signup, SignupRequest} from "@/lib/accountApiCaller";
import {useRouter} from "next/navigation";
import {BaseResponse} from "@/lib/models";

/**
 * 회원가입 폼 컴포넌트
 */
export default function SignUpForm() {
    const router = useRouter()

    const mutation = useMutation({
        mutationKey: ['signup'],
        mutationFn: (request: SignupRequest): Promise<BaseResponse<string>> => signup(request),
        onSuccess: data => {
            window.alert(data.body)
            router.push('/login')
        },
        onError: error => {
            window.alert(error.message)
        }
    })

    const handleSignup = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        const formData: FormData = new FormData(event.target as HTMLFormElement)

        mutation.mutate({
            nickname: formData.get('nickname')!.toString(),
            username: formData.get('username')!.toString(),
            password: formData.get('password')!.toString(),
        },)
    }

    return (
        <div className="flex-1 flex flex-col justify-center items-center p-4">
            <div className="w-full max-w-md px-4">
                <h1 className="text-4xl font-bold text-center mb-12">Sign up</h1>
                <form className="space-y-8" onSubmit={handleSignup}>
                    <div className="relative">
                        <FocusingInput id="nickname" labelName="Name" type="text"/>
                    </div>
                    <div className="relative">
                        <FocusingInput id="username" labelName="ID" type="text"/>
                    </div>
                    <div className="relative">
                        <FocusingInput id="password" labelName="Password" type="password"/>
                    </div>
                    <div className="pt-8 flex flex-col items-center">
                        <button type="submit"
                                className="bg-gray-100 hover:bg-gray-300 text-black rounded-full py-3 px-12 text-lg font-medium">
                            Signup
                        </button>
                        <Link href="/login" className="mt-6 text-blue-500 hover:underline">
                            Already have an account? Login
                        </Link>
                    </div>
                </form>
            </div>
        </div>
    )
}