import {getAccessToken, initHeaders, validateResponse} from "@/lib/utils";
import {BaseResponse} from "@/lib/models";
import {EventSourcePolyfill} from "event-source-polyfill";

const baseUrl = process.env.NEXT_PUBLIC_API_SERVER_URL

export type Notification = {
    alarmText: string,
    alarmTime: string,
}

export type NotificationPage = {
    totalSize: number,
    pageNumber: number,
    feedAlarms: Notification[],
}

/**
 * 저장된 알림 목록 조회
 * @param page 페이지 번호
 */
export async function getNotifications(page: number = 0): Promise<BaseResponse<NotificationPage>> {
    if (page < 0) {
        page = 0
    }

    const response: Response = await fetch(`${baseUrl}/api/accounts/alarms?page=${page}`, {
        method: 'GET',
        headers: {
            ...initHeaders(),
        }
    })

    return validateResponse(response)
}

/**
 * 알림 수신 이벤트 연결
 * @param onMessageCallback 알림 수신 콜백 함수
 * @param onErrorCallback 알림 수신 실패 콜백 함수
 * @return EventSourcePolyfill 연결된 알림 수신 이벤트
 */
export function subscribeAlarms(onMessageCallback: (message: string) => void, onErrorCallback: () => void): EventSourcePolyfill {
    let token: string = ''
    try {
        token = getAccessToken()
    } catch (error) {
        console.log(error)
        window.location.replace('/login')
    }

    const eventSource: EventSourcePolyfill = new EventSourcePolyfill(`${baseUrl}/api/accounts/alarms/sub`, {
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'text/event-stream',
        },
        heartbeatTimeout: 86_400_000 // 24h
    })

    eventSource.addEventListener('message', async (event) => {
        console.log(event.data)
    })

    eventSource.onmessage = (event) => {
        console.log("receive message", event.data)
        onMessageCallback(event.data)
    }

    eventSource.onerror = (event) => {
        console.log(event)
        onErrorCallback()
    }

    return eventSource
}