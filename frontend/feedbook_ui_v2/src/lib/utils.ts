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

export async function validateResponse(response: Response) {
    if (response.ok) {
        return response.json()
    } else {
        const errorMessage: string = await response.text()
        throw new Error(errorMessage)
    }
}
