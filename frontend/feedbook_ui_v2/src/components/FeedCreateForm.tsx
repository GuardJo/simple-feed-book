"use client"

import {Card} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import {ChangeEvent, FormEvent, useState} from "react";
import {useRouter} from "next/navigation";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {FeedCreateRequest, saveNewFeed} from "@/lib/feedApiCaller";
import {cn} from "@/lib/utils";
import {Loader2} from "lucide-react";

/**
 * 신규 피드 생성 Form 컴포넌트
 */
export default function FeedCreateForm() {
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");

    const router = useRouter();
    const queryClient = useQueryClient();

    const createFeedMutation = useMutation({
        mutationKey: ["saveNewFeed"],
        mutationFn: (request: FeedCreateRequest) => saveNewFeed(request),
        onSuccess: () => {
            setIsSubmitting(false);
            queryClient.invalidateQueries({
                queryKey: ["getFeeds"],
                refetchType: "all"
            })
            router.push("/feeds");
        },
        onError: (error) => {
            setIsSubmitting(false)
            setErrorMessage(error.message);
        }
    })

    const handleChangeTitle = (e: ChangeEvent<HTMLInputElement>) => {
        setTitle(e.target.value);
    }

    const handleChangeContent = (e: ChangeEvent<HTMLTextAreaElement>) => {
        setContent(e.target.value);
    }

    const handleSubmitted = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsSubmitting(true)
        createFeedMutation.mutate({title, content});
    }

    const handleCancel = () => {
        if (window.confirm("작성 중인 내용이 저장되지 않습니다. 정말 취소하시겠습니까?")) {
            router.push("/feeds");
        }
    }

    return (
        <Card className="p-6">
            <form onSubmit={handleSubmitted} className="space-y-6">
                {errorMessage && <div className="bg-red-50 text-red-500 p-3 rounded-md text-sm">{errorMessage}</div>}
                <div className="space-y-6">
                    <label htmlFor="title" className="text-sm font-medium">
                        제목
                    </label>
                    <input className="w-full border rounded-md p-2 focus:border-blue-500 focus:outline-none"
                           id="title"
                           type="text"
                           value={title}
                           onChange={handleChangeTitle}
                           placeholder="제목을 입력하세요."
                           disabled={isSubmitting}/>
                </div>
                <div className="space-y-2">
                    <label htmlFor="content" className="text-sm font-medium">
                        내용
                    </label>
                    <textarea
                        className="w-full border rounded-md p-2 focus:border-blue-500 focus:outline-none min-h-[200px]"
                        id="content"
                        value={content}
                        onChange={handleChangeContent}
                        placeholder="내용을 입력하세요."
                        disabled={isSubmitting}/>
                </div>
                <div className="flex justify-end space-x-3 pt-4">
                    <Button type="button" variant="outline" onClick={handleCancel} disabled={isSubmitting}>취소</Button>
                    <Button type="submit"
                            className={cn("bg-blue-500 hover:bg-blue-600 text-white", isSubmitting && "opacity-70 cursor-not-allowed")}
                            disabled={isSubmitting}>
                        {isSubmitting ? (
                            <>
                                <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                                저장 중...
                            </>
                        ) : ("저장")}
                    </Button>
                </div>
            </form>
        </Card>
    )
}