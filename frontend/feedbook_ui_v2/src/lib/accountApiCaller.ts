import {BaseResponse} from "@/lib/models";
import {initHeaders, validateResponse} from "@/lib/utils";

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

/**
 * 현재 접속 사용자 검증 요청
 */
export async function authenticate(): Promise<BaseResponse<string>> {
    const response: Response = await fetch(`${baseUrl}/api/auth`, {
        method: 'GET',
        headers: {
            ...initHeaders(),
        },
    })

    return await validateResponse(response)
}

/**
 * 로그아웃 API 요청
 * <hr/>
 * <i>기존 토큰 만료 처리</i>
 */
export async function logout(): Promise<BaseResponse<string>> {
    const response: Response = await fetch(`${baseUrl}/api/logout`, {
        method: 'POST',
        headers: {
            ...initHeaders(),
        },
    })

    return await validateResponse(response)
}