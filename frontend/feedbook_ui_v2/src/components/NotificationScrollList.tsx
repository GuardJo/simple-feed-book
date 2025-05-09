"use client"

import {getNotifications, Notification} from "@/lib/notificationApiCaller";
import NotificationItem from "@/components/NotificationItem";
import {useCallback, useEffect, useRef, useState} from "react";
import Loading from "@/app/loading";
import {useQuery} from "@tanstack/react-query";

export default function NotificationScrollList() {
    const [notifications, setNotifications] = useState<Notification[]>([])
    const [page, setPage] = useState(0)

    const observerRef = useRef<IntersectionObserver>(null)
    const loadMoreRef = useRef<HTMLDivElement>(null)

    const {data, isError, error, isLoading} = useQuery({
        queryKey: ['getNotifications', {page}],
        queryFn: () => getNotifications(page)
    })

    const observerCallback = useCallback((entries: IntersectionObserverEntry[]) => {
        const onLoadMore = (): void => {
            if (isLoading) {
                return
            }

            console.log("Loading more noti...")

            if (isError) {
                console.log(`getNotifications Error : ${error.message}`)
                window.alert('알림 목록을 가져오는데 실패하였습니다.')
            } else {
                if (data !== undefined) {
                    const newNotifications = data.body.feedAlarms

                    if (newNotifications.length > 0) {
                        setNotifications((prevState: Notification[]) => [...prevState, ...newNotifications])
                        setPage((prevState: number) => prevState + 1)
                    }
                }
            }
        }

        const [entry] = entries
        if (entry.isIntersecting && !isLoading) {
            console.log("Intersecting...")
            onLoadMore()
        }
    }, [data, isError, error, isLoading])

    useEffect(() => {
        const currentRef = loadMoreRef.current

        if (currentRef) {
            observerRef.current = new IntersectionObserver(observerCallback, {
                root: null,
                rootMargin: '100px',
                threshold: 0.1
            })

            observerRef.current.observe(currentRef)
        }

        return () => {
            if (observerRef.current && currentRef) {
                observerRef.current.unobserve(currentRef)
            }
        }
    }, [observerCallback]);

    return (
        <div className="space-y-3">
            {notifications.map((notification, index) => (
                <NotificationItem key={index} notification={notification}/>
            ))}
            <div ref={loadMoreRef} className="py-8 flex justify-center">
                {isLoading ? <Loading/> : <div className="h-10"/>}
            </div>
        </div>
    )
}