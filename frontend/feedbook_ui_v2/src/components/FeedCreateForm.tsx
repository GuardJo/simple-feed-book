"use client"

import {Card} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import {ChangeEvent, FormEvent, useState} from "react";
import {useRouter} from "next/navigation";

/**
 * 신규 피드 생성 Form 컴포넌트
 */
export default function FeedCreateForm() {
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");

    const router = useRouter();

    const handleChangeTitle = (e: ChangeEvent<HTMLInputElement>) => {
        setTitle(e.target.value);
    }

    const handleChangeContent = (e: ChangeEvent<HTMLTextAreaElement>) => {
        setContent(e.target.value);
    }

    const handleSubmitted = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        // TODO API 연동
        console.log(`title = ${title}, content = ${content}`);
    }

    const handleCancel = () => {
        if (window.confirm("작성 중인 내용이 저장되지 않습니다. 정말 취소하시겠습니까?")) {
            router.push("/feeds");
        }
    }

    return (
        <Card className="p-6">
            <form onSubmit={handleSubmitted} className="space-y-6">
                <div className="space-y-6">
                    <label htmlFor="title" className="text-sm font-medium">
                        제목
                    </label>
                    <input className="w-full border rounded-md p-2 focus:border-blue-500 focus:outline-none"
                           id="title"
                           type="text"
                           value={title}
                           onChange={handleChangeTitle}
                           placeholder="제목을 입력하세요."/>
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
                        placeholder="내용을 입력하세요."/>
                </div>
                <div className="flex justify-end space-x-3 pt-4">
                    <Button type="button" variant="outline" onClick={handleCancel}>취소</Button>
                    <Button type="submit" className="bg-blue-500 hover:bg-blue-600 text-white">저장</Button>
                </div>
            </form>
        </Card>
    )
}