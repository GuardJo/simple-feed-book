import {Meta, StoryObj} from "@storybook/react";
import FeedScrollList from "@/components/FeedScrollList";

const meta = {
    title: "components/FeedScrollList",
    component: FeedScrollList,
} satisfies Meta<typeof FeedScrollList>

export default meta

type Story = StoryObj<typeof meta>

export const AllFeeds: Story = {
    args: {}
}

export const MyFeeds: Story = {
    args: {
        onlyMe: true
    }
}
