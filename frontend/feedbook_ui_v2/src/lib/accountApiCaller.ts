import {BaseResponse} from "@/lib/models";
import {validateResponse} from "@/lib/utils";

const baseUrl = process.env.NEXT_PUBLIC_API_SERVER_URL

export type SignupRequest = {
    nickname: string,
    username: string,
    password: string
}

export type LoginRequest = {
    username: string,
    password: string
}

/**
 * 회원가입 API 요청
 * @param request 사용자명, ID, password
 */
export async function signup(request: SignupRequest): Promise<BaseResponse<string>> {
    const response: Response = await fetch(`${baseUrl}/api/signup`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(request)
    });

    return await validateResponse(response)
}

/**
 * 로그인 API 요청
 * @param request ID, password
 */
export async function login(request: LoginRequest): Promise<BaseResponse<string>> {
    const response: Response = await fetch(`${baseUrl}/api/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(request)
    });

    return await validateResponse(response)
}