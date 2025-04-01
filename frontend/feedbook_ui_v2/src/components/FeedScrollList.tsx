"use client"

import {Feed, getFeeds} from "@/lib/feedApiCaller";
import {useCallback, useEffect, useRef, useState} from "react";
import FeedItem from "@/components/FeedItem";
import Spinner from "@/components/Spinner";
import {useQuery} from "@tanstack/react-query";

/**
 * Feed 스크롤링 목록 컴포넌트
 */
export default function FeedScrollList() {
    const [feeds, setFeeds] = useState<Feed[]>([])
    const [page, setPage] = useState(0)
    const [isLast, setIsLast] = useState(false)

    const observerRef = useRef<IntersectionObserver>(null)
    const loadMoreRef = useRef<HTMLDivElement>(null)

    const {data, isLoading, isError, error} = useQuery({
        queryKey: ['getFeeds', {page}],
        queryFn: () => getFeeds(page)
    })

    const observerCallback = useCallback((entries: IntersectionObserverEntry[]) => {
        const onLoadMore = (): void => {
            if (isLoading) {
                return
            }

            console.log("Lading more feeds...")

            if (isError) {
                console.log(`getFeedQuery Error : ${error.message}`)
                window.alert('피드를 가져오는데 실패하였습니다.')
            } else {
                if (data !== undefined) {
                    const newFeeds: Feed[] = data.body.feeds
                    setFeeds((prevState: Feed[]) => [...prevState, ...newFeeds])
                    setPage((prevState) => prevState + 1)

                    if (newFeeds.length === 0) {
                        setIsLast(true)
                    }
                }
            }
        }

        const [entry] = entries
        if (entry.isIntersecting && !isLoading && !isLast) {
            console.log("Intersection detected")
            onLoadMore()
        }
    }, [isLast, data, isError, error, isLoading])

    useEffect(() => {
        const currentRef = loadMoreRef.current

        if (currentRef) {
            observerRef.current = new IntersectionObserver(observerCallback, {
                root: null,
                rootMargin: "100px",
                threshold: 0.1
            })

            observerRef.current.observe(currentRef)
            console.log("Observe set up")
        }

        return () => {
            if (observerRef.current && currentRef) {
                observerRef.current.unobserve(currentRef)
            }
        }
    }, [observerCallback]);

    return (
        <div className="space-y-4">
            {feeds.map((feed) => (
                <FeedItem key={feed.id} feed={feed}/>
            ))}

            <div ref={loadMoreRef} className="py-8 flex justify-center">
                {isLoading ? <Spinner/> : <div className="h-10"></div>}
            </div>
        </div>
    )
}