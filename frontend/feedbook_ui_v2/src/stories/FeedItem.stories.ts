import {Meta, StoryObj} from "@storybook/react";
import FeedItem from "@/components/FeedItem";

const meta = {
    title: "components/FeedItem",
    component: FeedItem
} satisfies Meta<typeof FeedItem>

export default meta

type Story = StoryObj<typeof meta>

export const MyFeed: Story = {
    args: {
        feed: {
            id: 1,
            title: "title1",
            content: "test content",
            author: "Tester1",
            isOwner: true,
            isFavorite: true,
            totalFavorites: 1
        },
    }
}

export const Feed: Story = {
    args: {
        feed: {
            id: 2,
            title: "title222",
            content: "test content",
            author: "Tester1",
            isOwner: false,
            isFavorite: false,
            totalFavorites: 0
        }
    }
}