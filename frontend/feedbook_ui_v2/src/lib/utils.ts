import {type ClassValue, clsx} from "clsx"
import {twMerge} from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
    return twMerge(clsx(inputs))
}

/**
 * 토큰을 LocalStorage에 저장한다.
 * @param token accessToken
 */
export function setAccessToken(token: string): void {
    localStorage.setItem("sfbToken", token)
}
