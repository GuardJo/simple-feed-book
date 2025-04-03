"use client"

import {Edit, Save, Trash2, X} from "lucide-react";
import FeedLikeButton from "@/components/FeedLikeButton";
import {Feed, FeedUpdateRequest, removeFeed, updateFeed} from "@/lib/feedApiCaller";
import ActionAlert from "@/components/ActionAlert";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {ChangeEvent, useState} from "react";

/**
 * 피드 컴포넌트
 */
export default function FeedItem({feed}: FeedItemProps) {
    const queryClient = useQueryClient()

    const [isDeleted, setIsDeleted] = useState(false)
    const [isEditMode, setIsEditMode] = useState(false)
    const [feedTitle, setFeedTitle] = useState(feed.title)
    const [feedContent, setFeedContent] = useState(feed.content)

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

    const feedUpdateMutation = useMutation({
        mutationKey: ['updateFeed', feed.id],
        mutationFn: (request: FeedUpdateRequest) => updateFeed(request),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ['getFeeds'],
                refetchType: "all"
            })
            setIsEditMode(false)
        },
        onError: (error) => {
            window.alert(error.message)
        }
    })

    const handleEditFeed = () => {
        setIsEditMode(true)
    }

    const handleChangeTitle = (e: ChangeEvent<HTMLInputElement>) => {
        setFeedTitle(e.target.value)
    }

    const handleChangeContent = (e: ChangeEvent<HTMLTextAreaElement>) => {
        setFeedContent(e.target.value)
    }

    const handleUpdateFeed = () => {
        console.log(`Updated feed, id = ${feed.id}`)
        feedUpdateMutation.mutate({
            feedId: feed.id,
            title: feedTitle,
            content: feedContent
        })
    }

    const handleRemoveFeed = () => {
        console.log(`Remove feed, feedId = ${feed.id}`)
        feedRemoveMutation.mutate(feed.id)
    }

    return (
        <div className="rounded-lg border bg-white p-4 shadow-sm" hidden={isDeleted}>
            <div className="flex justify-between items-start mb-2">
                {isEditMode ?
                    <input type="text"
                           className="flex-1 border-b border-gray-300 bg-transparent py-1 focus:border-blue-500 text-lg font-medium"
                           value={feedTitle} onChange={handleChangeTitle}
                           placeholder="제목" autoFocus/> :
                    <h3 className="text-lg font-medium">{feedTitle}</h3>
                }
                <div className="flex space-x-2">
                    {(isEditMode) ?
                        <>
                            <ActionAlert onAction={handleUpdateFeed} title="피드 저장" description="저장하시겠습니까?"
                                         actionType="저장">
                                <div className="text-green-500 hover:text-green-700" aria-label="save">
                                    <Save size={18}/>
                                </div>
                            </ActionAlert>
                            <button className="text-gray-500 hover:text-gray-700" onClick={() => setIsEditMode(false)}
                                    aria-label="cancel">
                                <X size={18}/>
                            </button>
                        </> :
                        <>
                            <button onClick={handleEditFeed} className="text-gray-500 hover:text-gray-700"
                                    aria-label="edit"
                                    hidden={!feed.isOwner}>
                                <Edit size={18}/>
                            </button>
                            <ActionAlert onAction={handleRemoveFeed} title="피드 삭제" description="피드를 삭제하시겠습니까?"
                                         actionType="삭제">
                                <div className="text-gray-600 hover:text-red-500" aria-label="remove"
                                     hidden={!feed.isOwner}>
                                    <Trash2 size={18}/>
                                </div>
                            </ActionAlert>
                        </>}
                </div>
            </div>

            <div className="mb-2 text-sm text-gray-600">{feed.author}</div>

            {(isEditMode) ?
                <div className="mb-4">
                    <textarea
                        className="w-full border rounded-md p-2 focus:border-blue-500 focus:outline-none min-h-[100px]"
                        placeholder="내용을 입력하세요." value={feedContent} onChange={handleChangeContent}/>
                </div> :
                <div className="mb-4">{feedContent}</div>
            }

            <div className="flex justify-between items-center">
                <FeedLikeButton feedId={feed.id} isLike={feed.isFavorite} defaultCount={feed.totalFavorites}
                                disabled={isEditMode}/>
                <button className="text-sm text-blue-600 hover:underline">Comments...</button>
            </div>
        </div>
    )
}

interface FeedItemProps {
    feed: Feed
}