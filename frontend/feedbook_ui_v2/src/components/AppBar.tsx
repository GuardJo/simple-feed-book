"use client"

import React, {useState} from "react";
import {Button} from "@/components/ui/button";
import {Menu} from "lucide-react";
import {cn} from "@/lib/utils";
import Link from "next/link";

/**
 * App Bar
 */
export default function AppBar({children}: AppbarProps) {
    const [openSidebar, setOpenSidebar] = useState(false)

    return (
        <>
            <header className="sticky top-0 z-10 flex h-14 items-center border-b bg-gray-100 px-4">
                <Button variant="ghost" size="icon" onClick={() => setOpenSidebar(true)} className="mr-2">
                    <Menu className="h-5 w-5"/>
                    <span className="sr-only">메뉴 열기</span>
                </Button>
                <Link href="/">
                    <h1 className="text-xl font-medium">Simple Feed Book</h1>
                </Link>
                {openSidebar && <div className="fixed inset-0 z-20 bg-black/50" onClick={() => setOpenSidebar(false)}/>}
                <div className={cn(
                    "fixed inset-y-0 left-0 z-30 w-64 transform bg-white transition-transform duration-300 ease-in-out",
                    openSidebar ? "translate-x-0" : "-translate-x-full",
                )}>
                    <div className="flex h-14 items-center border-b px-4">
                        <h2 className="text-lg font-medium">Menu</h2>
                    </div>
                    <nav className="flex flex-col">
                        <Link href="/feeds" className="border-b py-3 px-4 hover:bg-gray-50">
                            All Feeds
                        </Link>
                        <Link href="/my-feeds" className="border-b py-3 px-4 hover:bg-gray-50">
                            My Feeds
                        </Link>
                        <Link href="/edit-feeds" className="border-b py-3 px-4 hover:bg-gray-50">
                            Write Feed
                        </Link>
                        <Link href="/alarms" className="border-b py-3 px-4 hover:bg-gray-50">
                            Alarms
                        </Link>
                        <div className="mt-auto p-4">
                            <Button asChild
                                    className="w-full bg-gray-100 hover:bg-gray-300 text-black">
                                <Link href="/login" className="flex justify-center items-center ">
                                    Login
                                </Link>
                            </Button>
                        </div>
                    </nav>
                </div>
            </header>
            {children}
        </>
    )
}

interface AppbarProps {
    children: React.ReactNode
}