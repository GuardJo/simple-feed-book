import {BaseResponse} from "@/lib/models";

const baseUrl = process.env.NEXT_PUBLIC_API_SERVER_URL

export type SignupRequest = {
    nickname: string,
    username: string,
    password: string
}

export async function signup(request: SignupRequest): Promise<BaseResponse<string>> {
    const response: Response = await fetch(`${baseUrl}/api/signup`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(request)
    });

    if (response.ok) {
        return response.json()
    } else {
        const errorMessage: string = await response.text()
        throw new Error(errorMessage)
    }
}
