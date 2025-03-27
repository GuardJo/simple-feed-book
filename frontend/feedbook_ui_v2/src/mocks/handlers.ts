import {http, HttpResponse} from "msw";

const mockUrl = process.env.NEXT_PUBLIC_API_MOCK_URL

export const handlers = [
    http.post(`${mockUrl}/api/signup`, () => {
        return HttpResponse.json({
            status: 'OK',
            body: 'SUCCESSES'
        })
    }),
    http.post(`${mockUrl}/api/login`, () => {
        return HttpResponse.json({
            status: 'OK',
            body: 'TestToken'
        })
    })
]