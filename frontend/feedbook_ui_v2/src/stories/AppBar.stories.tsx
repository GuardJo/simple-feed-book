import {Meta, StoryObj} from "@storybook/react";
import AppBar from "@/components/AppBar";

const meta = {
    title: "layouts/AppBar",
    component: AppBar,
    tags: ["autodocs"],
    parameters: {
        docs: {
            story: {
                inline: false,
                iframeHeight: 400
            }
        }
    }
} satisfies Meta<typeof AppBar>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        children: <div><h1>Test Page</h1></div>
    }
}