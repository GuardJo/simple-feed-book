import {initHeaders, validateResponse} from "@/lib/utils";
import {BaseResponse} from "@/lib/models";

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