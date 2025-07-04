import {type ClassValue, clsx} from "clsx"
import {twMerge} from "tailwind-merge"

const tokenName: string = process.env.NEXT_PUBLIC_API_TOKEN_NAME ?? ''

export function cn(...inputs: ClassValue[]) {
    return twMerge(clsx(inputs))
}

/**
 * 토큰을 LocalStorage에 저장한다.
 * @param token accessToken
 */
export function setAccessToken(token: string): void {
    localStorage.setItem(tokenName, token)
}

/**
 * 저장된 토큰을 반환한다
 * @throws Error 존재하지 않을 경우, 에러 발생
 */
export function getAccessToken(): string {
    const accessToken = localStorage.getItem(tokenName)

    if (accessToken === null) {
        throw new Error("Not Found AccessToken")
    } else {
        return accessToken
    }
}

/**
 * 인증 토큰을 헤더에 추가하여 반환한다.
 */
export function initHeaders(): HeadersInit {
    let token: string = ''
    try {
        token = getAccessToken()
    } catch (error) {
        console.log(error)
        window.location.replace('/login')
    }

    return {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    }
}

export async function validateResponse(response: Response) {
    if (response.ok) {
        return response.json()
    } else if (response.status === 401) {
        console.log('go to login page')
        window.location.replace('/login')
    } else {
        const errorMessage: string = await response.text()
        throw new Error(errorMessage)
    }
}
