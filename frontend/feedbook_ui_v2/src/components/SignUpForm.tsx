"use client"

import FocusingInput from "@/components/FocusingInput";
import Link from "next/link";
import {FormEvent} from "react";

export default function SignUpForm() {
    const handleSignup = (e: FormEvent) => {
        e.preventDefault()
        // TODO API 연동
        console.log("Signup!")
    }

    return (
        <div className="flex-1 flex flex-col justify-center items-center p-4">
            <div className="w-full max-w-md px-4">
                <h1 className="text-4xl font-bold text-center mb-12">Sign up</h1>
                <form className="space-y-8">
                    <div className="relative">
                        <FocusingInput id="nickname" labelName="Nickname" type="text"/>
                    </div>
                    <div className="relative">
                        <FocusingInput id="username" labelName="ID" type="text"/>
                    </div>
                    <div className="relative">
                        <FocusingInput id="password" labelName="Password" type="password"/>
                    </div>
                    <div className="pt-8 flex flex-col items-center">
                        <button type="submit"
                                onClick={handleSignup}
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