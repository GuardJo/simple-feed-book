"use client"

import {Notification} from "@/lib/notificationApiCaller";
import NotificationItem from "@/components/NotificationItem";
import {useCallback, useEffect, useRef, useState} from "react";
import Loading from "@/app/loading";

export default function NotificationScrollList() {
    const [notifications, setNotifications] = useState<Notification[]>([])
    const [isLoading, setIsLoading] = useState(false)

    const observerRef = useRef<IntersectionObserver>(null)
    const loadMoreRef = useRef<HTMLDivElement>(null)

    const observerCallback = useCallback((entries: IntersectionObserverEntry[]) => {
        const onLoadMore = (): void => {
            if (isLoading) {
                return
            }

            setIsLoading(true)
            console.log("Loading more noti...")

            const mockNotifications: Notification[] = [
                {alarmText: 'test1', alarmTime: '1시간'},
                {alarmText: 'test2', alarmTime: '1시간'},
                {alarmText: 'test3', alarmTime: '1시간'},
            ]

            // TODO API 연동
            setTimeout(() => {
                setNotifications((prevState: Notification[]) => [...prevState, ...mockNotifications])
                setIsLoading(false)
            }, 500)
        }

        const [entry] = entries
        if (entry.isIntersecting && !isLoading) {
            console.log("Intersecting...")
            onLoadMore()
        }
    }, [])

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
            {notifications.map((notification) => (
                <NotificationItem notification={notification}/>
            ))}
            <div ref={loadMoreRef} className="py-8 flex justify-center">
                {isLoading ? <Loading/> : <div className="h-10"/>}
            </div>
        </div>
    )
}