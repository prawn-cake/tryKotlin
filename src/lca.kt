/**
 * Lowest common ancestor of a Binary Tree
 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/
 */

import kotlin.test.assertEquals


data class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)


/**
 * Just a helper to validate the tree, not used in the algorithm
 */
fun dfs(node: TreeNode?) {
    if (node == null) {
        return
    }
    println(node.value)
    dfs(node.left)
    dfs(node.right)
}

fun buildTree(arr: List<Int?>): List<TreeNode> {
    // Convert arr of ints into nodes, keep nulls to indicate empty leaves
    val convertedArr = arr.map {
        when (it) {
            null -> null
            else -> TreeNode(it)
        }
    }

    // Create links between the nodes
    for ((index: Int, node: TreeNode?) in convertedArr.withIndex()) {
        val left = 2 * index + 1
        val right = 2 * index + 2

        if (node != null) {
            if (left < arr.size && arr[left] != null)
                node.left = convertedArr[left]

            if (right < arr.size && arr[right] != null)
                node.right = convertedArr[right]
        }
    }

    // Clean the tree
    return convertedArr.filterNotNull()
}

fun lca(root: TreeNode?, p: Int, q: Int): TreeNode? {
    // We reached the leaf, time to bubble up
    if (root == null) {
        return null
    }

    // Using post-order traverse, inspecting subtrees first and then decide
    val leftSubtree = lca(root.left, p, q)
    val rightSubtree = lca(root.right, p, q)

    // We cover 2 cases: when p and q in different subtrees or in the same (left or right)
    val isInDifferentSubtrees = (leftSubtree != null && rightSubtree != null)

    // We already found lca, just propagate it up
    if (isInDifferentSubtrees)
        return root

    val isTargetNode = root.value == p || root.value == q
    if (isTargetNode)
        return root

    // We need to propagate found value up to trigger the checks above
    if (!isTargetNode && leftSubtree != null) {
        return leftSubtree
    }
    if (!isTargetNode && rightSubtree != null) {
        return rightSubtree
    }

    return null
}

fun main() {
    val arr = listOf(3, 5, 1, 6, 2, 0, 8, null, null, 7, 4)
    val tree: List<TreeNode> = buildTree(arr)  // convert array into a tree

    val root = tree[0]
    // dfs(root)  // Tree check

    assertEquals(3, lca(root, 5, 1)?.value)
    assertEquals(5, lca(root, 5, 4)?.value)
    assertEquals(3, lca(root, 3, 7)?.value)
    assertEquals(5, lca(root, 5, 5)?.value)
    assertEquals(3, lca(root, 7, 0)?.value)
    assertEquals(3, lca(root, 0, 7)?.value)
}