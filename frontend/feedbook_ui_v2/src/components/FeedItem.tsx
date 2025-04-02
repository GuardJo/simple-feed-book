"use client"

import {Edit, Trash2} from "lucide-react";
import FeedLikeButton from "@/components/FeedLikeButton";
import {Feed, removeFeed} from "@/lib/feedApiCaller";
import ActionAlert from "@/components/ActionAlert";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {useState} from "react";

/**
 * 피드 컴포넌트
 */
export default function FeedItem({feed}: FeedItemProps) {
    const queryClient = useQueryClient()
    const [isDeleted, setIsDeleted] = useState(false)
    const feedRemoveMutation = useMutation({
        mutationKey: ['removeFeed', feed.id],
        mutationFn: (id: number) => removeFeed(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ["getFeeds"],
                refetchType: "all"
            })
            setIsDeleted(true)
        },
        onError: (error) => {
            window.alert(error.message)
        }
    })

    const handleEditFeed = () => {
        console.log(`Edit feed, feedId = ${feed.id}`)
    }

    const handleRemoveFeed = () => {
        console.log(`Remove feed, feedId = ${feed.id}`)
        feedRemoveMutation.mutate(feed.id)
    }

    return (
        <div className="rounded-lg border bg-white p-4 shadow-sm" hidden={isDeleted}>
            <div className="flex justify-between items-start mb-2">
                <h3 className="text-lg font-medium">{feed.title}</h3>
                <div className="flex space-x-2">
                    <button onClick={handleEditFeed} className="text-gray-500 hover:text-gray-700" aria-label="edit"
                            hidden={!feed.isOwner}>
                        <Edit size={18}/>
                    </button>
                    <ActionAlert onAction={handleRemoveFeed} title="피드 삭제" description="피드를 삭제하시겠습니까?">
                        <div className="text-gray-600 hover:text-red-500" aria-label="remove"
                             hidden={!feed.isOwner}>
                            <Trash2 size={18}/>
                        </div>
                    </ActionAlert>
                </div>
            </div>

            <div className="mb-2 text-sm text-gray-600">{feed.author}</div>

            <div className="mb-4">{feed.content}</div>

            <div className="flex justify-between items-center">
                <FeedLikeButton isLike={feed.isFavorite} defaultCount={feed.totalFavorites}/>
                <button className="text-sm text-blue-600 hover:underline">Comments...</button>
            </div>
        </div>
    )
}

interface FeedItemProps {
    feed: Feed
}