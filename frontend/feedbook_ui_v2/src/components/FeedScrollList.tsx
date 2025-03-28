"use client"

import {Feed} from "@/lib/feedApiCaller";
import {useCallback, useEffect, useRef, useState} from "react";
import FeedItem from "@/components/FeedItem";
import Spinner from "@/components/Spinner";

/**
 * Feed 스크롤링 목록 컴포넌트
 */
export default function FeedScrollList() {
    const [feeds, setFeeds] = useState<Feed[]>([])
    const [page, setPage] = useState(0)
    const [loading, setLoading] = useState(false)

    const observerRef = useRef<IntersectionObserver>(null)
    const loadMoreRef = useRef<HTMLDivElement>(null)

    const observerCallback = useCallback((entries: IntersectionObserverEntry[]) => {
        const onLoadMore = (): void => {
            if (loading) {
                return
            }

            setLoading(true)
            console.log("Lading more feeds...")

            // TODO API 연동
            setTimeout(() => {
                const newFeeds: Feed[] = [{
                    id: page,
                    title: "title1",
                    content: "test content",
                    author: "Tester1",
                    isOwner: true,
                    isFavorite: true,
                    totalFavorites: 1
                }]
                setFeeds((prevState: Feed[]) => [...prevState, ...newFeeds])
                setPage((prevState) => prevState + 1)
                setLoading(false)
            }, 800)
        }

        const [entry] = entries
        if (entry.isIntersecting && !loading) {
            console.log("Intersection detected")
            onLoadMore()
        }
    }, [page, loading])

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
                {loading ? <Spinner/> : <div className="h-10"></div>}
            </div>
        </div>
    )
}